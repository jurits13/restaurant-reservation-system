function RecommendationSummary({ result, searchRequest }) {
    const { tables = [], recommendations = [], bestTableId = null } = result;

    const bestRecommendation = recommendations.find(
        (recommendation) => recommendation.tableId === bestTableId
    );

    const bestTable = tables.find((table) => table.id === bestTableId);

    return (
        <div
            style={{
                margin: "20px auto",
                maxWidth: "700px",
                padding: "16px",
                border: "1px solid #ccc",
                borderRadius: "12px",
                backgroundColor: "#f9f9f9",
                textAlign: "left"
            }}
        >
            <h2 style={{ marginTop: 0 }}>Recommendation summary</h2>

            <p>
                <strong>Date:</strong> {searchRequest.date}
            </p>
            <p>
                <strong>Time:</strong> {searchRequest.time}
            </p>
            <p>
                <strong>Party size:</strong> {searchRequest.partySize}
            </p>

            {bestTable && bestRecommendation ? (
                <>
                    <hr />
                    <p>
                        <strong>Best recommendation:</strong> {bestTable.name}
                    </p>
                    <p>
                        <strong>Score:</strong> {bestRecommendation.score}
                    </p>
                    <p>
                        <strong>Zone:</strong> {bestTable.zone}
                    </p>

                    <div>
                        <strong>Reasons:</strong>
                        <ul>
                            {bestRecommendation.reasons.map((reason, index) => (
                                <li key={index}>{reason}</li>
                            ))}
                        </ul>
                    </div>
                </>
            ) : (
                <>
                    <hr />
                    <p>No suitable table found for the selected filters.</p>
                </>
            )}
        </div>
    );
}

export default RecommendationSummary;