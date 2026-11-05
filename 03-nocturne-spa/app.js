const API_BASE_URL = new URLSearchParams(location.search).get("api") || "http://localhost:8080/api";
const demoServices = [
  { id: 1, name: "Signature Haircut", category: "Hair", staffId: 1, staff: "Maya Chen", duration: 45, price: 58, description: "A considered cut, shaped around you.", icon: "✂", tone: "peach" },
  { id: 2, name: "Haircut & Style", category: "Hair", staffId: 2, staff: "Theo James", duration: 60, price: 72, description: "Cut, finish and a little fresh perspective.", icon: "✦", tone: "lavender" },
  { id: 3, name: "Deep Tissue Massage", category: "Massage", staffId: 3, staff: "Nina Patel", duration: 60, price: 95, description: "Focused pressure to release built-up tension.", icon: "◉", tone: "sage" },
  { id: 4, name: "Calm & Restore Facial", category: "Skin", staffId: 4, staff: "Amara Lewis", duration: 50, price: 82, description: "A gentle reset for your skin and senses.", icon: "✿", tone: "rose" },
  { id: 5, name: "Brow Shape & Tint", category: "Skin", staffId: 1, staff: "Maya Chen", duration: 30, price: 38, description: "Soft definition, tailored to your features.", icon: "⌁", tone: "sand" },
  { id: 6, name: "Express Blowout", category: "Hair", staffId: 2, staff: "Theo James", duration: 35, price: 44, description: "A polished finish, ready for wherever next.", icon: "〰", tone: "blue" },
  { id: 7, name: "Aromatherapy Massage", category: "Massage", staffId: 3, staff: "Nina Patel", duration: 75, price: 110, description: "Slow, soothing bodywork with botanical oils.", icon: "❋", tone: "mint" },
  { id: 8, name: "Glow Renewal Facial", category: "Skin", staffId: 4, staff: "Amara Lewis", duration: 65, price: 105, description: "A brightening treatment with a lasting glow.", icon: "☼", tone: "yellow" }
];
const fallbackStaff = [
  { id: 1, name: "Maya Chen", title: "Senior stylist" },
  { id: 2, name: "Theo James", title: "Style specialist" },
  { id: 3, name: "Nina Patel", title: "Massage therapist" },
  { id: 4, name: "Amara Lewis", title: "Skin therapist" }
];
let services = [...demoServices];
let staffMembers = [...fallbackStaff];
let apiOnline = false;
let toastTimer;
const $ = selector => document.querySelector(selector);
const grid = $("#service-grid");
const search = $("#search");
const category = $("#category");
const staff = $("#staff");
const empty = $("#empty-state");
const count = $("#result-count");
const dialog = $("#booking-dialog");

function escapeHtml(value) {
  return String(value).replace(/[&<>"']/g, char => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[char]));
}
function fillFilter(select, values, firstLabel) {
  select.innerHTML = `<option value="all">${escapeHtml(firstLabel)}</option>` +
    values.map(value => `<option value="${escapeHtml(value)}">${escapeHtml(value)}</option>`).join("");
}
function fillBookingStaff(selectedId) {
  $("#booking-staff").innerHTML = `<option value="">Choose a specialist</option>` +
    staffMembers.map(person => `<option value="${person.id}" ${Number(person.id) === Number(selectedId) ? "selected" : ""}>${escapeHtml(person.name)}</option>`).join("");
}
function populateFilters() {
  fillFilter(category, [...new Set(services.map(service => service.category))].sort(), "All categories");
  fillFilter(staff, [...new Set(services.map(service => service.staff))].sort(), "Any specialist");
}
function filteredServices() {
  const term = search.value.trim().toLocaleLowerCase();
  return services.filter(service =>
    (category.value === "all" || service.category === category.value) &&
    (staff.value === "all" || service.staff === staff.value) &&
    (!term || `${service.name} ${service.category} ${service.staff} ${service.description}`.toLocaleLowerCase().includes(term))
  );
}
function render() {
  const results = filteredServices();
  count.textContent = `${results.length} ${results.length === 1 ? "service" : "services"}`;
  grid.innerHTML = results.map((service, index) => `
    <article class="service-card" style="--card-delay:${index * 45}ms">
      <div class="service-visual ${escapeHtml(service.tone)}"><span class="service-icon" aria-hidden="true">${escapeHtml(service.icon)}</span><span class="service-category">${escapeHtml(service.category)}</span></div>
      <div class="service-content"><div class="service-topline"><span>${service.duration} MIN</span><span>WITH ${escapeHtml(service.staff.toUpperCase())}</span></div>
        <h3 class="service-name">${escapeHtml(service.name)}</h3><p>${escapeHtml(service.description)}</p>
        <div class="service-bottom"><strong>£${Number(service.price).toFixed(0)}</strong><button class="book-button" type="button" data-book="${service.id}">Book <span aria-hidden="true">↗</span></button></div>
      </div>
    </article>`).join("");
  empty.hidden = results.length !== 0;
  grid.hidden = results.length === 0;
}
function showToast(message) {
  const toast = $("#toast");
  toast.textContent = message;
  toast.style.opacity = "1";
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => { toast.style.opacity = "0"; }, 3500);
}
function localDateString(date) {
  const offset = date.getTimezoneOffset();
  return new Date(date.getTime() - offset * 60000).toISOString().slice(0, 10);
}
function resetFilters() {
  search.value = "";
  category.value = "all";
  staff.value = "all";
  render();
  search.focus();
}
async function loadBackendData() {
  try {
    const [serviceResponse, staffResponse] = await Promise.all([
      fetch(`${API_BASE_URL}/services`),
      fetch(`${API_BASE_URL}/staff`)
    ]);
    if (!serviceResponse.ok || !staffResponse.ok) throw new Error("Booking API returned an error");
    const apiServices = await serviceResponse.json();
    const apiStaff = await staffResponse.json();
    if (!Array.isArray(apiServices) || !Array.isArray(apiStaff)) throw new Error("Booking API returned invalid data");
    services = apiServices;
    staffMembers = apiStaff;
    apiOnline = true;
    showToast("Connected to the booking service.");
  } catch (error) {
    apiOnline = false;
    services = [...demoServices];
    staffMembers = [...fallbackStaff];
    console.info("Booking API is unavailable; using preview data.", error);
  }
  populateFilters();
  fillBookingStaff();
  render();
}
async function refreshAvailableTimes() {
  if (!apiOnline) return;
  const service = services.find(item => String(item.id) === $("#selected-service-id").value);
  const staffId = $("#booking-staff").value;
  const date = $("#booking-date").value;
  if (!service || !staffId || !date) return;
  const select = $("#booking-time");
  const selectedTime = select.value;
  select.innerHTML = `<option value="">Loading times…</option>`;
  try {
    const params = new URLSearchParams({ serviceId: service.id, staffId, date });
    const response = await fetch(`${API_BASE_URL}/availability?${params}`);
    if (!response.ok) throw new Error("Could not load availability");
    const times = await response.json();
    select.innerHTML = times.length
      ? `<option value="">Choose a time</option>` + times.map(time => {
          const shortTime = time.slice(0, 5);
          return `<option value="${shortTime}">${shortTime}</option>`;
        }).join("")
      : `<option value="">No times available</option>`;
    if (times.some(time => time.slice(0, 5) === selectedTime)) select.value = selectedTime;
  } catch (error) {
    select.innerHTML = `<option value="">Choose a time</option>`;
    showToast("Could not load times. Please try again.");
  }
}

$("#year").textContent = new Date().getFullYear();
const tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate() + 1);
$("#booking-date").min = localDateString(new Date());
$("#booking-date").value = localDateString(tomorrow);
search.addEventListener("input", render);
category.addEventListener("change", render);
staff.addEventListener("change", render);
$("#reset").addEventListener("click", resetFilters);
$("#empty-reset").addEventListener("click", resetFilters);
$("#booking-date").addEventListener("change", refreshAvailableTimes);
$("#booking-staff").addEventListener("change", refreshAvailableTimes);
grid.addEventListener("click", event => {
  const button = event.target.closest("[data-book]");
  if (!button) return;
  const service = services.find(item => String(item.id) === button.dataset.book);
  if (!service) return;
  $("#selected-service").value = service.name;
  $("#selected-service-id").value = service.id;
  $("#dialog-title").textContent = `Book ${service.name}`;
  $("#dialog-service-summary").textContent = `${service.duration} minutes · £${Number(service.price).toFixed(0)} · ${service.category}`;
  fillBookingStaff(service.staffId);
  $("#booking-form").hidden = false;
  $("#confirmation").hidden = true;
  dialog.showModal();
  refreshAvailableTimes();
});
$("#booking-form").addEventListener("submit", async event => {
  event.preventDefault();
  if (!event.currentTarget.reportValidity()) return;
  const service = services.find(item => String(item.id) === $("#selected-service-id").value);
  const person = staffMembers.find(item => String(item.id) === $("#booking-staff").value);
  const dateValue = $("#booking-date").value;
  const time = $("#booking-time").value;
  const submit = event.currentTarget.querySelector("button[type='submit']");
  submit.disabled = true;
  submit.textContent = "Confirming…";
  try {
    let confirmationText;
    if (apiOnline) {
      const response = await fetch(`${API_BASE_URL}/bookings`, {
        method: "POST",
        headers: { "Content-Type": "application/json", "Accept": "application/json" },
        body: JSON.stringify({
          serviceId: Number(service.id), staffId: Number(person.id), date: dateValue,
          time, clientName: $("#guest-name").value.trim(), clientEmail: $("#guest-email").value.trim()
        })
      });
      const payload = await response.json();
      if (!response.ok) throw new Error(payload.detail || payload.message || "Booking could not be completed.");
      confirmationText = `${payload.service} with ${payload.staff} on ${new Date(`${payload.date}T00:00:00`).toLocaleDateString(undefined, { weekday: "long", month: "long", day: "numeric" })} at ${String(payload.time).slice(0, 5)}. Confirmation sent to ${payload.clientEmail}.`;
    } else {
      const date = new Date(`${dateValue}T00:00:00`).toLocaleDateString(undefined, { weekday: "long", month: "long", day: "numeric" });
      confirmationText = `${service.name} with ${person.name} on ${date} at ${time}. Preview only; connect the API to save this appointment.`;
    }
    $("#confirmation-details").textContent = confirmationText;
    $("#booking-form").hidden = true;
    $("#confirmation").hidden = false;
  } catch (error) {
    showToast(error.message || "Booking failed. Please try another time.");
    await refreshAvailableTimes();
  } finally {
    submit.disabled = false;
    submit.innerHTML = 'Confirm appointment <span aria-hidden="true">→</span>';
  }
});
$("#done-button").addEventListener("click", () => dialog.close());
dialog.addEventListener("click", event => { if (event.target === dialog) dialog.close(); });
populateFilters();
fillBookingStaff();
render();
loadBackendData();
