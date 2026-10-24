# Employee Manager

A Spring Boot employee CRUD application using Thymeleaf, Spring Data JPA,
Lombok, and MySQL.

## Run locally

1. Start MySQL (for example, the MySQL module in XAMPP) and create the database:

   ```sql
   CREATE DATABASE employee_db;
   ```

2. Optionally copy `secrets.properties.example` to `secrets.properties` and set
   the MySQL username and password. `secrets.properties` is ignored by Git.
   With a default XAMPP installation, the configured `root` user and empty
   password work without this file.
3. Start the application from the project directory:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

4. Open <http://localhost:8080>. Hibernate creates or updates the `employees`
   table in `employee_db`.

## Employee routes

| Method | URL | Action |
| --- | --- | --- |
| GET | `/employees` | List employees; optionally filter with `?keyword=name` |
| POST | `/employees` | Create an employee |
| GET | `/employees/{id}/edit` | Show the edit form |
| POST | `/employees/{id}/update` | Update an employee |
| POST | `/employees/{id}/delete` | Delete an employee |

The POST routes accept form URL-encoded fields: `fullName`, `email`, `phone`,
`department`, and `salary`.
