function TableItem({ table, reserved, best, good, onClick }) {
    let backgroundColor = "#cccccc";
    let border = "2px solid black";
    let cursor = "pointer";
    let boxShadow = "none";

    if (reserved) {
        backgroundColor = "#e74c3c";
        cursor = "not-allowed";
    } else if (best) {
        backgroundColor = "#2ecc71";
        border = "4px solid gold";
        boxShadow = "0 0 12px rgba(255, 215, 0, 0.9)";
    } else if (good) {
        backgroundColor = "#7bed9f";
        border = "3px solid #27ae60";
    }

    return (
        <div
            onClick={!reserved ? onClick : undefined}
            style={{
                position: "absolute",
                left: table.x,
                top: table.y,
                width: table.width,
                height: table.height,
                backgroundColor,
                border,
                boxShadow,
                cursor,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                fontWeight: "bold",
                borderRadius: "8px"
            }}
            title={`${table.name} • ${table.capacity} seats • ${table.zone}`}
        >
            {best ? `⭐ ${table.name}` : table.name}
        </div>
    );
}

export default TableItem;