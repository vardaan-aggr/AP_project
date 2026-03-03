**Role-based desktop ERP application for managing university academics with secure authentication and maintenance controls.**

---

### 🔐 Authentication & Security
- Dual-database architecture (separate **Auth DB** and **ERP DB**)
- Secure password storage using **bcrypt/argon2 hashing** (UNIX shadow-style)
- Login with role-based dashboard redirection
- Change Password feature
- Account lockout after multiple failed attempts
- Strict role-based access control (RBAC)
- Maintenance Mode with global read-only enforcement
- Access validation before every data modification
- No plaintext passwords stored anywhere

---

### 👨‍🎓 Student Features
- Browse searchable/sortable course catalog
- Register for sections (capacity & duplicate checks)
- Drop sections before deadline
- View personalized timetable
- View detailed grades (component-wise + final grade)
- Download transcript (CSV + PDF)
- Enrollment validation & prerequisite checks
- Prevention of duplicate registrations

---

### 👨‍🏫 Instructor Features
- View only assigned sections
- Define assessment components (quiz/midterm/end-sem, etc.)
- Enter and update scores
- Automatic final grade computation (custom weightage rule)
- View class statistics (averages, distributions)
- Export grades (CSV)
- Blocked from modifying unassigned sections

---

### 👨‍💼 Admin Features
- Create users (students/instructors/admins)
- Create and manage courses
- Create/edit sections (room, time, capacity, semester/year)
- Assign instructors to sections
- Toggle Maintenance Mode (with live UI banner)
- Backup & restore ERP database
- Data integrity validation

---

### 🛠 System & Technical Features
- Clean layered architecture:
  - UI Layer (Swing)
  - Service Layer (business logic enforcement)
  - Access Control Layer
  - Auth Layer
  - Data Layer (JDBC + connection pooling)
- Connection pooling (HikariCP)
- Input validation & error handling
- Duplicate enrollment prevention (DB constraints)
- Referential integrity checks
- Sortable/filterable tables
- Responsive UI with modern Look & Feel (FlatLaf)
- Logging (SLF4J / Logback)
- Schema migration support (optional Flyway/Liquibase)
- Export support (CSV, PDF)
- Backup via CLI integration
- Full acceptance & edge-case testing coverage
- Seed scripts for automated DB setup

---

### 🧪 Testing & Quality
- Acceptance, edge-case, negative, and integrity testing
- Role enforcement tested across flows
- Maintenance Mode enforced globally
- ~100-course performance tested
- Clean separation of concerns (UI ↔ Service ↔ DB)

---
