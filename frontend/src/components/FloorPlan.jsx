import TableItem from "./TableItem";

function FloorPlan({ result, searchRequest, onSelectTable }) {
    const {
        tables = [],
        reservations = [],
        bestTableId = null,
        goodTableIds = []
    } = result;

    function isReserved(tableId) {
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

    return (
        <div style={{ position: "relative", width: 1100, height: 500 }}>
            {tables.map((table) => {
                const reserved = isReserved(table.id);
                const best = table.id === bestTableId;
                const good = goodTableIds.includes(table.id);

                return (
                    <TableItem
                        key={table.id}
                        table={table}
                        reserved={reserved}
                        best={best}
                        good={good}
                        onClick={() => onSelectTable(table)}
                    />
                );
            })}
        </div>
    );
}

export default FloorPlan;