import { useState } from "react";
import { createReservation } from "../services/api";

function ReservationForm({ table, searchRequest, onReservationCreated }) {
    const [name, setName] = useState("");

    async function reserve() {
        if (!name.trim()) {
            alert("Please enter customer name");
            return;
        }

        try {
            await createReservation({
                customerName: name,
                date: searchRequest.date,
                time: searchRequest.time,
                partySize: searchRequest.partySize,
                tableId: table.id
            });

            setName("");
            onReservationCreated();
            alert("Reserved!");
        } catch (e) {
            alert(e.message);
        }
    }

    return (
        <div>
            <h3 style={{ marginBottom: "10px" }}>Make reservation</h3>

            <input
                placeholder="Customer name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                style={{
                    width: "100%",
                    padding: "8px",
                    marginBottom: "10px",
                    boxSizing: "border-box"
                }}
            />

            <button onClick={reserve}>
                Reserve table
            </button>
        </div>
    );
}

export default ReservationForm;