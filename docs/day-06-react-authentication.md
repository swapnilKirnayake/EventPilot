# Day 6 - React Authentication

## Completed

- Added React Router for frontend route management.
- Added frontend authentication API functions.
- Added JWT token storage using localStorage.
- Added authenticated request support with Bearer tokens.
- Added AuthContext for centralized authentication state.
- Added useAuth hook for accessing authentication state.
- Added ProtectedRoute for authenticated routes.
- Added Login page.
- Added Register page.
- Connected React authentication flows to Spring Boot authentication APIs.
- Added authenticated dashboard placeholder.
- Added logout functionality.
- Added authentication persistence across browser refreshes.

## Routes

- `/` - EventPilot home
- `/login` - Login
- `/register` - Registration
- `/dashboard` - Protected dashboard

## Authentication Flow

Register and login requests are sent from the React frontend to the Spring Boot backend through the Vite `/api` proxy.

Successful authentication returns a JWT and user information.

The frontend stores the JWT and user information locally and restores authentication state after a browser refresh.

## Protected Routes

Unauthenticated users attempting to access `/dashboard` are redirected to `/login`.

Authenticated users can access the dashboard.

Logout clears the stored authentication state and prevents further access to protected routes.

## Verification

Frontend lint:

`npm run lint`

Result:

- Passed with 0 errors.

Frontend production build:

`npm run build`

Result:

- Build successful.

Browser integration verification:

- Registration completed successfully.
- Login completed successfully.
- JWT authentication verified.
- Dashboard access verified.
- User information displayed.
- Authentication persisted after browser refresh.
- Logout verified.
- Protected dashboard redirected unauthenticated users to `/login`.

Day 6 React authentication implementation is complete.