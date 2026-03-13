import { useState } from "react";

function SearchForm({ onSearch }) {
    const [date, setDate] = useState("");
    const [time, setTime] = useState("");
    const [partySize, setPartySize] = useState(2);
    const [zone, setZone] = useState("");
    const [windowSeat, setWindowSeat] = useState(false);
    const [quiet, setQuiet] = useState(false);
    const [accessible, setAccessible] = useState(false);
    const [nearKidsArea, setNearKidsArea] = useState(false);

    function handleSubmit(e) {
        e.preventDefault();

        if (!date) {
            alert("Please select a date");
            return;
        }

        if (!time) {
            alert("Please select a time");
            return;
        }

        if (Number(partySize) < 1) {
            alert("Party size must be at least 1");
            return;
        }

        onSearch({
            date,
            time,
            partySize: Number(partySize),
            zone,
            windowSeat,
            quiet,
            accessible,
            nearKidsArea
        });
    }

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                required
            />

            <input
                type="time"
                value={time}
                onChange={(e) => setTime(e.target.value)}
                required
            />

            <input
                type="number"
                min="1"
                value={partySize}
                onChange={(e) => setPartySize(e.target.value)}
            />

            <select value={zone} onChange={(e) => setZone(e.target.value)}>
                <option value="">Any zone</option>
                <option value="HALL">Hall</option>
                <option value="TERRACE">Terrace</option>
                <option value="PRIVATE">Private</option>
            </select>

            <label>
                <input
                    type="checkbox"
                    checked={windowSeat}
                    onChange={(e) => setWindowSeat(e.target.checked)}
                />
                Window
            </label>

            <label>
                <input
                    type="checkbox"
                    checked={quiet}
                    onChange={(e) => setQuiet(e.target.checked)}
                />
                Quiet
            </label>

            <label>
                <input
                    type="checkbox"
                    checked={accessible}
                    onChange={(e) => setAccessible(e.target.checked)}
                />
                Accessible
            </label>

            <label>
                <input
                    type="checkbox"
                    checked={nearKidsArea}
                    onChange={(e) => setNearKidsArea(e.target.checked)}
                />
                Near kids area
            </label>

            <button type="submit">Search</button>
        </form>
    );
}

export default SearchForm;