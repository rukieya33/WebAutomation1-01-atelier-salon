import React, { useEffect, useMemo, useRef, useState } from "react";

const DEMO_SERVICES = [
  { id: 1, name: "Signature Haircut", category: "Hair", staffId: 1, staff: "Maya Chen", duration: 45, price: 58, description: "A considered cut, shaped around you.", icon: "✂", tone: "peach" },
  { id: 2, name: "Haircut & Style", category: "Hair", staffId: 2, staff: "Theo James", duration: 60, price: 72, description: "Cut, finish and a little fresh perspective.", icon: "✦", tone: "lavender" },
  { id: 3, name: "Deep Tissue Massage", category: "Massage", staffId: 3, staff: "Nina Patel", duration: 60, price: 95, description: "Focused pressure to release built-up tension.", icon: "◉", tone: "sage" },
  { id: 4, name: "Calm & Restore Facial", category: "Skin", staffId: 4, staff: "Amara Lewis", duration: 50, price: 82, description: "A gentle reset for your skin and senses.", icon: "✿", tone: "rose" },
  { id: 5, name: "Brow Shape & Tint", category: "Skin", staffId: 1, staff: "Maya Chen", duration: 30, price: 38, description: "Soft definition, tailored to your features.", icon: "⌁", tone: "sand" },
  { id: 6, name: "Express Blowout", category: "Hair", staffId: 2, staff: "Theo James", duration: 35, price: 44, description: "A polished finish, ready for wherever next.", icon: "〰", tone: "blue" },
  { id: 7, name: "Aromatherapy Massage", category: "Massage", staffId: 3, staff: "Nina Patel", duration: 75, price: 110, description: "Slow, soothing bodywork with botanical oils.", icon: "❋", tone: "mint" },
  { id: 8, name: "Glow Renewal Facial", category: "Skin", staffId: 4, staff: "Amara Lewis", duration: 65, price: 105, description: "A brightening treatment with a lasting glow.", icon: "☼", tone: "yellow" }
];
const DEMO_STAFF = [
  { id: 1, name: "Maya Chen", title: "Senior stylist" },
  { id: 2, name: "Theo James", title: "Style specialist" },
  { id: 3, name: "Nina Patel", title: "Massage therapist" },
  { id: 4, name: "Amara Lewis", title: "Skin therapist" }
];
const BRANDS = {
  atelier: { path: "/atelier", theme: "atelier", name: "MAISON & MANE", mark: "M", kicker: "SALON · SKIN · BODY", headline: "A little time, just for you.", intro: "A considered collection of hair, skin and body rituals, made personal by people who care.", note: "Thoughtful care\nstarts here.", title: "Find your service", about: "Good care\nfeels personal.", how: "Three steps to feeling good." },
  clinic: { path: "/clinic", theme: "clinic", name: "GOODWELL", mark: "G", kicker: "WELLNESS · CARE · YOU", headline: "Care that fits your life.", intro: "Book trusted wellness and care appointments with specialists who take the time to listen.", note: "Care that\nlistens.", title: "Find your care", about: "Well-being\nstarts here.", how: "Three steps to feeling better." },
  nocturne: { path: "/nocturne", theme: "nocturne", name: "STILL / HOUSE", mark: "S", kicker: "A QUIETER KIND OF CARE", headline: "Find your way back to calm.", intro: "Slow down with restorative massage and skin treatments in a space made for exhaling.", note: "Make space\nto exhale.", title: "Choose a ritual", about: "Slow down.\nCome back to you.", how: "Three gentle steps to reset." },
  metro: { path: "/metro", theme: "metro", name: "SIDEQUEST", mark: "S", kicker: "LOCAL SERVICES · EASY BOOKINGS", headline: "Your next good thing starts here.", intro: "Discover local specialists, compare services and book a little time for yourself.", note: "Good people.\nGood services.", title: "Find your next thing", about: "Local talent.\nEasy to book.", how: "Three quick steps to booked." },
  bloom: { path: "/bloom", theme: "bloom", name: "BLOOM ROOM", mark: "B", kicker: "BEAUTY · BODY · BALANCE", headline: "Make room for feeling good.", intro: "Small rituals. Lovely people. A fresh start whenever you need one.", note: "A little ritual\ngoes a long way.", title: "Pick your little ritual", about: "A moment\nfor yourself.", how: "Three lovely steps to feeling good." }
};
const API_BASE = new URLSearchParams(window.location.search).get("api") || "http://localhost:8080/api";
const TIMES = ["09:00", "10:30", "12:00", "13:30", "15:00", "16:30"];
function localDate(date) { const shifted = new Date(date.getTime() - date.getTimezoneOffset() * 60000); return shifted.toISOString().slice(0, 10); }
function tomorrow() { const date = new Date(); date.setDate(date.getDate() + 1); return localDate(date); }

export default function App() {
  const brand = Object.values(BRANDS).find(item => item.path === window.location.pathname.replace(/\/$/, "")) || BRANDS.atelier;
  const [services, setServices] = useState(DEMO_SERVICES);
  const [staff, setStaff] = useState(DEMO_STAFF);
  const [apiOnline, setApiOnline] = useState(false);
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState("all");
  const [staffFilter, setStaffFilter] = useState("all");
  const [selected, setSelected] = useState(null);
  const [bookingStaff, setBookingStaff] = useState("");
  const [date, setDate] = useState(tomorrow);
  const [time, setTime] = useState("");
  const [times, setTimes] = useState(TIMES);
  const [guest, setGuest] = useState({ name: "", email: "" });
  const [confirmation, setConfirmation] = useState("");
  const [toast, setToast] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const dialog = useRef(null);
  const categories = useMemo(() => [...new Set(services.map(item => item.category))].sort(), [services]);
  const results = useMemo(() => {
    const term = query.trim().toLocaleLowerCase();
    return services.filter(item => (category === "all" || item.category === category) && (staffFilter === "all" || item.staff === staffFilter) && (!term || `${item.name} ${item.category} ${item.staff} ${item.description}`.toLocaleLowerCase().includes(term)));
  }, [services, query, category, staffFilter]);

  useEffect(() => {
    document.body.className = brand.theme;
    document.body.dataset.theme = brand.theme;
    document.title = `${brand.name} — Book your appointment`;
  }, [brand]);
  useEffect(() => {
    let active = true;
    Promise.all([fetch(`${API_BASE}/services`), fetch(`${API_BASE}/staff`)]).then(async ([serviceRes, staffRes]) => {
      if (!serviceRes.ok || !staffRes.ok) throw new Error("Booking API unavailable");
      const [apiServices, apiStaff] = await Promise.all([serviceRes.json(), staffRes.json()]);
      if (!Array.isArray(apiServices) || !Array.isArray(apiStaff)) throw new Error("Invalid API response");
      if (active) { setServices(apiServices); setStaff(apiStaff); setApiOnline(true); }
    }).catch(() => { if (active) { setServices(DEMO_SERVICES); setStaff(DEMO_STAFF); setApiOnline(false); } });
    return () => { active = false; };
  }, []);
  useEffect(() => {
    if (!apiOnline || !selected || !bookingStaff || !date) { setTimes(TIMES); return; }
    const params = new URLSearchParams({ serviceId: selected.id, staffId: bookingStaff, date });
    fetch(`${API_BASE}/availability?${params}`).then(response => {
      if (!response.ok) throw new Error("Could not load times");
      return response.json();
    }).then(values => setTimes(values.map(value => String(value).slice(0, 5)))).catch(() => setTimes([]));
  }, [apiOnline, selected, bookingStaff, date]);
  useEffect(() => { if (toast) { const timer = setTimeout(() => setToast(""), 3500); return () => clearTimeout(timer); } }, [toast]);

  function openBooking(service) {
    setSelected(service); setBookingStaff(String(service.staffId || "")); setTime(""); setConfirmation(""); setDate(tomorrow());
    dialog.current?.showModal();
  }
  function clearFilters() { setQuery(""); setCategory("all"); setStaffFilter("all"); }
  async function submitBooking(event) {
    event.preventDefault();
    setSubmitting(true);
    try {
      let text;
      if (apiOnline) {
        const response = await fetch(`${API_BASE}/bookings`, { method: "POST", headers: { "Content-Type": "application/json", Accept: "application/json" }, body: JSON.stringify({ serviceId: Number(selected.id), staffId: Number(bookingStaff), date, time, clientName: guest.name.trim(), clientEmail: guest.email.trim() }) });
        const payload = await response.json();
        if (!response.ok) throw new Error(payload.detail || payload.message || "Booking could not be completed.");
        text = `${payload.service} with ${payload.staff} on ${payload.date} at ${String(payload.time).slice(0, 5)}. Confirmation sent to ${payload.clientEmail}.`;
      } else {
        const person = staff.find(member => String(member.id) === String(bookingStaff));
        text = `${selected.name} with ${person?.name || selected.staff} on ${date} at ${time}. Preview only; connect the API to save this appointment.`;
      }
      setConfirmation(text);
    } catch (error) { setToast(error.message || "Booking failed. Please try another time."); }
    finally { setSubmitting(false); }
  }

  return <>
    <header className="site-header">
      <a className="brand" href="#top" aria-label={`${brand.name} home`}><span className="brand-mark">{brand.mark}</span><span>{brand.name}</span></a>
      <nav className="main-nav" aria-label="Main navigation"><a href="#services">Services</a><a href="#how">How it works</a><a href="#about">About</a></nav>
      <div className="app-switcher"><label className="sr-only" htmlFor="app-choice">Choose a concept</label><select id="app-choice" value={brand.path} onChange={event => { window.location.href = event.target.value; }}>{Object.values(BRANDS).map(item => <option key={item.path} value={item.path}>{item.name}</option>)}</select></div>
      <a className="header-cta" href="#services">Find a time <span aria-hidden="true">↗</span></a>
    </header>
    <main id="top">
      <section className="hero" id="about">
        <div className="hero-copy"><p className="eyebrow"><span className="eyebrow-dot" />{brand.name} · {brand.kicker}</p><h1>{brand.headline}</h1><p className="hero-description">{brand.intro}</p><a className="hero-link" href="#services">Explore appointments <span aria-hidden="true">↓</span></a></div>
        <div className="hero-art" aria-hidden="true"><div className="art-orbit orbit-one"/><div className="art-orbit orbit-two"/><div className="art-sun"/><div className="art-card"><span>YOUR TIME, WELL SPENT</span><strong>01 — 05</strong></div></div>
        <div className="hero-note"><span className="note-star">✳</span><span>{brand.note.split("\n").map((line, index) => <React.Fragment key={line}>{index > 0 && <br/>}{line}</React.Fragment>)}</span></div>
      </section>
      <section className="booking-area" id="services">
        <div className="section-heading"><div><p className="eyebrow">THE MENU</p><h2>{brand.title}</h2></div><p className="result-count" aria-live="polite">{results.length} {results.length === 1 ? "service" : "services"}{apiOnline ? " · live" : " · preview"}</p></div>
        <div className="toolbar"><label className="search-box"><span aria-hidden="true">⌕</span><span className="sr-only">Search services</span><input type="search" placeholder="Search a service…" autoComplete="off" value={query} onChange={event => setQuery(event.target.value)}/></label>
          <label className="select-wrap"><span className="sr-only">Filter by category</span><select value={category} onChange={event => setCategory(event.target.value)}><option value="all">All categories</option>{categories.map(value => <option key={value}>{value}</option>)}</select></label>
          <label className="select-wrap"><span className="sr-only">Filter by staff member</span><select value={staffFilter} onChange={event => setStaffFilter(event.target.value)}><option value="all">Any specialist</option>{[...new Set(services.map(item => item.staff))].sort().map(value => <option key={value}>{value}</option>)}</select></label><button className="reset-button" type="button" onClick={clearFilters}>Reset</button>
        </div>
        <div className="content-grid"><aside className="side-note"><p className="eyebrow">A LITTLE ABOUT US</p><h3>{brand.about.split("\n").map((line, index) => <React.Fragment key={line}>{index > 0 && <br/>}{line}</React.Fragment>)}</h3><p>Choose a service, meet your specialist, and pick a time that works for you.</p><div className="side-stats"><div><strong>4.9</strong><span>Guest rating</span></div><div><strong>8+</strong><span>Years of care</span></div></div></aside>
          <div className="service-grid" aria-live="polite">{results.map((service, index) => <article className="service-card" key={service.id} style={{ "--card-delay": `${index * 45}ms` }}><div className={`service-visual ${service.tone || "peach"}`}><span className="service-icon" aria-hidden="true">{service.icon || "✦"}</span><span className="service-category">{service.category}</span></div><div className="service-content"><div className="service-topline"><span>{service.duration} MIN</span><span>WITH {service.staff.toUpperCase()}</span></div><h3 className="service-name">{service.name}</h3><p>{service.description}</p><div className="service-bottom"><strong>£{Number(service.price).toFixed(0)}</strong><button className="book-button" type="button" onClick={() => openBooking(service)}>Book <span aria-hidden="true">↗</span></button></div></div></article>)}</div>
        </div>
        {results.length === 0 && <div className="empty-state"><span className="empty-icon">⌕</span><h3>No services found</h3><p>Try another search or reset your filters.</p><button className="reset-button" type="button" onClick={clearFilters}>Clear filters</button></div>}
      </section>
      <section className="how-section" id="how"><p className="eyebrow">SIMPLE BY DESIGN</p><h2>{brand.how}</h2><div className="steps"><article><span>01</span><h3>Choose your service</h3><p>Browse the menu and find the right fit.</p></article><article><span>02</span><h3>Pick your specialist</h3><p>Meet the people who make the difference.</p></article><article><span>03</span><h3>Save your time</h3><p>Choose a convenient time and you’re set.</p></article></div></section>
    </main>
    <footer className="site-footer"><a className="brand" href="#top"><span className="brand-mark">{brand.mark}</span><span>{brand.name}</span></a><p>Made for your well-being. © {new Date().getFullYear()}</p><a href="#services">Back to services ↑</a></footer>
    <dialog className="booking-dialog" ref={dialog} aria-labelledby="dialog-title" onClick={event => { if (event.target === dialog.current) dialog.current.close(); }}>
      <form method="dialog" className="dialog-close-form"><button className="dialog-close" aria-label="Close booking dialog">×</button></form>
      {confirmation ? <div className="confirmation" role="status"><span className="confirmation-icon">✓</span><h3>You’re on the calendar.</h3><p>{confirmation}</p><button className="primary-button" type="button" onClick={() => dialog.current.close()}>Done</button></div> : selected && <><div className="dialog-heading"><p className="eyebrow">YOUR NEXT APPOINTMENT</p><h2 id="dialog-title">Book {selected.name}</h2><p>{selected.duration} minutes · £{Number(selected.price).toFixed(0)} · {selected.category}</p></div><form className="booking-form" onSubmit={submitBooking}><label>Specialist<select required value={bookingStaff} onChange={event => setBookingStaff(event.target.value)}>{staff.map(person => <option key={person.id} value={person.id}>{person.name}</option>)}</select></label><div className="form-row"><label>Date<input type="date" required min={localDate(new Date())} value={date} onChange={event => setDate(event.target.value)}/></label><label>Time<select required value={time} onChange={event => setTime(event.target.value)}><option value="">Choose a time</option>{times.map(value => <option key={value} value={value}>{value}</option>)}</select></label></div><label>Your name<input required autoComplete="name" placeholder="Alex Morgan" value={guest.name} onChange={event => setGuest({ ...guest, name: event.target.value })}/></label><label>Email address<input required type="email" autoComplete="email" placeholder="alex@example.com" value={guest.email} onChange={event => setGuest({ ...guest, email: event.target.value })}/></label><button className="primary-button" type="submit" disabled={submitting}>{submitting ? "Confirming…" : <>Confirm appointment <span aria-hidden="true">→</span></>}</button></form></>}
    </dialog>
    <div className="toast" role="status" aria-live="polite" style={{ opacity: toast ? 1 : 0 }}>{toast}</div>
  </>;
}

