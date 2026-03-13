import ReservationForm from "./ReservationForm";

function SelectedTableDetails({ table, result, searchRequest, onReservationCreated }) {
    const { recommendations = [], bestTableId = null, goodTableIds = [], reservations = [] } = result;

    const recommendation = recommendations.find(
        (item) => item.tableId === table.id
    );

    const reserved = isReserved(table.id, reservations, searchRequest);
    const status = getTableStatus(table.id, reserved, bestTableId, goodTableIds);

    return (
        <div
            style={{
                width: "320px",
                padding: "16px",
                border: "1px solid #ccc",
                borderRadius: "12px",
                backgroundColor: "#f9f9f9",
                textAlign: "left"
            }}
        >
            <h2 style={{ marginTop: 0 }}>Selected table</h2>

            <p>
                <strong>Name:</strong> {table.name}
            </p>
            <p>
                <strong>Capacity:</strong> {table.capacity}
            </p>
            <p>
                <strong>Zone:</strong> {table.zone}
            </p>
            <p>
                <strong>Status:</strong> {status}
            </p>

            <div style={{ marginTop: "16px" }}>
                <strong>Features:</strong>
                <ul>
                    <li>Window seat: {table.windowSeat ? "Yes" : "No"}</li>
                    <li>Quiet: {table.quiet ? "Yes" : "No"}</li>
                    <li>Accessible: {table.accessible ? "Yes" : "No"}</li>
                    <li>Near kids area: {table.nearKidsArea ? "Yes" : "No"}</li>
                </ul>
            </div>

            {recommendation && (
                <div style={{ marginTop: "16px" }}>
                    <strong>Recommendation info:</strong>
                    <p>
                        <strong>Label:</strong> {recommendation.label}
                    </p>
                    <p>
                        <strong>Score:</strong> {recommendation.score}
                    </p>

                    <div>
                        <strong>Reasons:</strong>
                        <ul>
                            {recommendation.reasons.map((reason, index) => (
                                <li key={index}>{reason}</li>
                            ))}
                        </ul>
                    </div>
                </div>
            )}

            {!reserved ? (
                <div style={{ marginTop: "20px" }}>
                    <ReservationForm
                        table={table}
                        searchRequest={searchRequest}
                        onReservationCreated={onReservationCreated}
                    />
                </div>
            ) : (
                <p style={{ marginTop: "20px", color: "#c0392b", fontWeight: "bold" }}>
                    This table is already reserved for the selected time.
                </p>
            )}
        </div>
    );
}

function getTableStatus(tableId, reserved, bestTableId, goodTableIds) {
    if (reserved) {
        return "Reserved";
    }

    if (tableId === bestTableId) {
        return "Best match";
    }

    if (goodTableIds.includes(tableId)) {
        return "Good match";
    }

    return "Available";
}

function isReserved(tableId, reservations, searchRequest) {
    if (!searchRequest?.time) return false;

    const requestedStart = searchRequest.time;
    const requestedEnd = addTwoHours(requestedStart);

    return reservations.some((reservation) => {
        if (reservation.tableId !== tableId) return false;

        return (
            requestedStart < reservation.endTime &&
            requestedEnd > reservation.startTime
        );
    });
}

function addTwoHours(timeString) {
    const [hours, minutes] = timeString.split(":").map(Number);
    const date = new Date();
    date.setHours(hours, minutes, 0, 0);
    date.setHours(date.getHours() + 2);

    const hh = String(date.getHours()).padStart(2, "0");
    const mm = String(date.getMinutes()).padStart(2, "0");
    const ss = "00";

    return `${hh}:${mm}:${ss}`;
}

export default SelectedTableDetails;