# Day 4 - Security, JWT & Roles

## Completed

- Added Spring Security.
- Added JJWT API, implementation, and Jackson integration.
- Added JWT configuration through environment variables.
- Implemented JWT token generation and validation.
- Added JWT authentication filter using Bearer tokens.
- Added database-backed CustomUserDetailsService.
- Configured stateless Spring Security.
- Added role-based authorization rules:
  - ADMIN -> /api/admin/**
  - ORGANIZER / ADMIN -> /api/organizer/**
  - Public -> /api/health and /actuator/health
  - All other endpoints require authentication.
- Added JWT service tests.
- Verified application context startup.

## Security Notes

JWT secrets are supplied through the EVENTPILOT_JWT_SECRET environment variable and are not stored in source control.

## Verification

Command:

mvn clean test

Result:

- Tests run: 23
- Failures: 0
- Errors: 0
- Skipped: 0

Day 4 security foundation is complete.
