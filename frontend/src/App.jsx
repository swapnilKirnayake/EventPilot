import { useEffect, useState } from "react";
import { getHealth } from "./services/api";

function App() {
  const [health, setHealth] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    getHealth()
      .then(setHealth)
      .catch((err) => {
        setError(err.message);
      });
  }, []);

  return (
    <main>
      <h1>EventPilot</h1>

      {health && (
        <p>
          Backend: {health.status} | Application: {health.application}
        </p>
      )}

      {!health && !error && <p>Checking backend...</p>}

      {error && <p>Backend unavailable: {error}</p>}
    </main>
  );
}

export default App;