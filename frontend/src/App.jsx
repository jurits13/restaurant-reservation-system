import { useState } from "react";
import SearchForm from "./components/SearchForm";
import FloorPlan from "./components/FloorPlan";
import ReservationForm from "./components/ReservationForm";
import RecommendationSummary from "./components/RecommendationSummary";
import SelectedTableDetails from "./components/SelectedTableDetails";
import { searchTables } from "./services/api";

function App() {
    const [result, setResult] = useState(null);
    const [selectedTable, setSelectedTable] = useState(null);
    const [searchRequest, setSearchRequest] = useState(null);

    async function handleSearch(request) {
        try {
            const response = await searchTables(request);
            setResult(response);
            setSearchRequest(request);
            setSelectedTable(null);
        } catch (error) {
            alert(error.message);
            console.error(error);
        }
    }

    async function refreshSearchAfterReservation() {
        if (!searchRequest) return;

        try {
            const response = await searchTables(searchRequest);
            setResult(response);
            setSelectedTable(null);
        } catch (error) {
            alert(error.message);
            console.error(error);
        }
    }

    return (
        <div>
            <h1>Restaurant Reservation System</h1>

            <SearchForm onSearch={handleSearch} />

            <div style={{ marginTop: "10px", marginBottom: "20px" }}>
                <span style={{ marginRight: "20px" }}>🟥 Reserved</span>
                <span style={{ marginRight: "20px" }}>⭐ Best match</span>
                <span style={{ marginRight: "20px" }}>🟩 Good match</span>
                <span>⬜ Available</span>
            </div>

            {result && searchRequest && (
                <RecommendationSummary
                    result={result}
                    searchRequest={searchRequest}
                />
            )}

            {result && (
                <div
                    style={{
                        display: "flex",
                        gap: "24px",
                        alignItems: "flex-start",
                        justifyContent: "center",
                        marginTop: "20px",
                        flexWrap: "wrap"
                    }}
                >
                    <FloorPlan
                        result={result}
                        searchRequest={searchRequest}
                        onSelectTable={setSelectedTable}
                    />

                    {selectedTable && searchRequest && (
                        <SelectedTableDetails
                            table={selectedTable}
                            result={result}
                            searchRequest={searchRequest}
                            onReservationCreated={refreshSearchAfterReservation}
                        />
                    )}
                </div>
            )}
        </div>
    );
}

export default App;