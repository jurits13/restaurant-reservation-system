const API_BASE = "http://localhost:8080/api";

export async function searchTables(request) {
    const response = await fetch(`${API_BASE}/search`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.error || "Search request failed");
    }

    return data;
}

export async function createReservation(request) {
    const response = await fetch(`${API_BASE}/reservations`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.error || "Reservation failed");
    }

    return data;
}