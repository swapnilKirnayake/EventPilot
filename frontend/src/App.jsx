import { useEffect, useState } from "react";
import { getHealth } from "./services/api";

function App() {
  const [status, setStatus] = useState("Checking...");
  const [error, setError] = useState(null);

  useEffect(() => {
    getHealth()
      .then((data) => {
        setStatus(data.status);
      })
      .catch((err) => {
        setError(err.message);
        setStatus("DOWN");
      });
  }, []);

  return (
    <main>
      <h1>EventPilot</h1>
      <p>Backend status: {status}</p>

      {error && <p>{error}</p>}
    </main>
  );
}

export default App;