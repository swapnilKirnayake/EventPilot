# Day 5 - Authentication APIs

## Completed

- Added authentication request and response DTOs.
- Added registration API at POST /api/auth/register.
- Added login API at POST /api/auth/login.
- Added BCrypt password hashing.
- Added Spring Security AuthenticationManager and DaoAuthenticationProvider.
- Added database-backed authentication through UserRepository.
- Added JWT generation after successful registration and login.
- Added duplicate-email handling with HTTP 409.
- Added validation error handling with HTTP 400.
- Added invalid-credentials handling with HTTP 401.
- Added integration tests for authentication APIs.

## Endpoints

### Register

POST /api/auth/register

Creates a new USER account and returns a JWT.

### Login

POST /api/auth/login

Authenticates an existing user and returns a JWT.

## Security

- Passwords are never stored in plaintext.
- BCrypt is used for password hashing.
- JWT authentication remains stateless.
- Authentication endpoints are publicly accessible so users can register and log in.

## Verification

Command:

mvn clean test

Result:

- Tests run: 29
- Failures: 0
- Errors: 0
- Skipped: 0

Day 5 authentication API implementation is complete.
