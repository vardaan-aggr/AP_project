# AP Project

Java-based student ERP to manage the academics between students, professors, admins with security and fast.

## Overview
This project involves building a **Java Swing desktop application** to manage university operations like course registration, grading, and user administration. It supports three distinct roles—Student, Instructor, and Admin—and includes a global "Maintenance Mode" to restrict data changes when needed.

### Key Aims

* **Secure:** Protects user data through a separate **Auth DB**, using **bcrypt hashing** so real passwords are never stored.


* **Fast & Stable:** Designed for efficiency, such as loading large course catalogs in seconds, while ensuring the app does not crash during errors.


* **User-Friendly:** Features a clean, organized UI with sortable tables and clear, helpful feedback messages.


* **Data Integrity:** Uses strict access rules and input validation to prevent duplicate enrollments or nonsensical data entry.



## Files
- `src/` — Main source code
- `lib/` — External libraries (if any)
- `Report.pdf` / `Report.docx` — Detailed project report
- `Diagrams.png` — Architecture/use case/ER diagrams
- `instructions to run.docx` — Setup and execution guide

## Technologies
- Java (100%)

## How to Run
Refer to `instructions to run.docx` for detailed steps (likely involves compiling `src/` and running the main class via VS Code or command line).

For issues, check the project report.
