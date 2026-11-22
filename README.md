# JAHRVIS

> A personal command center for *all my stuff*.

Jahrvis is a self-hosted, single-user management system I'm building for myself. The idea is to have one calm place to keep track of the things I care about: books I'm reading, money coming in and out, budgets and savings goals, skills I want to learn and how far along I am, and my speedcubing journey.

It isn't a product. There are no users, no sign-up flow, and no plans to make it one. It's a personal project — equal parts useful tool and something I can show off.

## What exists today

The **books module** is the first fully working slice: a Spring Boot REST API backed by PostgreSQL and a Vue dashboard on the front end.

The books dashboard includes:

- A single-page dashboard with an at-a-glance stat strip (records, pages logged, completion %, active/recovered counts)
- Searchable and filterable book grid
- Add and edit forms in modals
- Delete confirmation in a modal
- A progress bar on every book card showing `pagesRead / totalPages`
- A separate inline **Log Pages** control on each card that reuses the same `PUT` update endpoint
- A deliberate wasteland/terminal aesthetic: charcoal surfaces, rust and amber signals, hazard stripes, scanlines, and monospace type, built with Tailwind CSS

## Planned modules

These are the ideas driving the project, not implemented yet:

- Finances: transactions, accounts, and cash-flow overview
- Budgets and savings goals with progress toward targets
- Learning tracker: things to learn, resources, and milestone progress
- Speedcubing log: solve times, averages, PBs, and training history
- A shared "home" dashboard once more than one module exists

## Stack

**Backend**

- Java 21, Spring Boot 4.1.1
- Spring Web MVC (REST API)
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Maven (wrapper included)

**Front end**

- Vue 3 with `<script setup>`
- Vite 8, Vue Router, Pinia
- Tailwind CSS v4 (via `@tailwindcss/vite`)

## Project layout

```
jahrvis/
├── pom.xml                     # Spring Boot backend
├── src/main/java/.../jahrvis/
│   ├── controllers/            # REST controllers (BookController)
│   ├── services/               # Business logic (BookService, BookServiceImpl)
│   ├── repositories/           # Spring Data repositories
│   ├── models/                 # JPA entities (Book)
│   └── enums/                  # Domain enums (ReadingStatus)
├── src/main/resources/
│   └── application.properties  # Datasource + JPA settings
└── client/                     # Vue 3 + Vite front end
    └── src/
        ├── api/                # fetch wrapper for the books API
        ├── stores/             # Pinia store
        ├── views/              # Dashboard page
        └── components/books/   # Card + modal components
```

## API

Base path: `/api/v1/books`

| Method | Path | Description | Success |
| --- | --- | --- | --- |
| `GET` | `/api/v1/books` | List all books | `200` with a JSON array |
| `POST` | `/api/v1/books` | Create a book | `201` with the created book |
| `PUT` | `/api/v1/books` | Update a book (full object, including `id`) | `200`, or `404` if missing |
| `DELETE` | `/api/v1/books/{id}` | Delete a book by id | `204`, or `404` if missing |

A book looks like this:

```json
{
  "id": 1,
  "title": "The Last Library",
  "totalPages": 320,
  "pagesRead": 96,
  "isReading": "READING"
}
```

`isReading` is one of `BACKLOG`, `READING`, `DONE`, or `RE_READ`.

## Getting started

### Prerequisites

- **JDK 21 or newer** for the backend. If your IDE reports `release version 21 not supported`, set the project SDK (and language level) to 21+ in *Project Structure*; the `pom.xml` deliberately targets Java 21.
- **PostgreSQL** running locally.
- **Node.js** `^22.18.0` or `>=24.12.0`, with npm.

### 1. Database

Create a database named `jahrvis`, then adjust the credentials in `src/main/resources/application.properties` if needed (defaults are `postgres` / `postgres`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jahrvis
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Hibernate uses `ddl-auto=update`, so the schema is created/updated automatically on startup. That's fine for a personal project, not for production.

### 2. Backend

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The API starts at `http://localhost:8080`.

### 3. Front end

```bash
cd client
npm install --legacy-peer-deps
npm run dev
```

`--legacy-peer-deps` is needed because the original Vue scaffold pins `oxlint` and `eslint-plugin-oxlint` to slightly different versions. It doesn't change any versions.

Open `http://localhost:5173`. The Vite dev server proxies `/api` to `http://localhost:8080`, so no CORS configuration is required.

### Useful commands

```bash
# backend
./mvnw compile
./mvnw test

# front end (from client/)
npm run dev
npm run build
npm run lint
npm run format
```

## Notes

- **No authentication.** This is intended to run locally for one person. 
- **Persistence is PostgreSQL only** for now; there is no seed data or migration tool yet.
- The UI is intentionally opinionated: one dashboard, modals for destructive or form-heavy actions, and a slightly worn-in, post-apocalyptic feel.

## License

Personal project. Not licensed for redistribution.
