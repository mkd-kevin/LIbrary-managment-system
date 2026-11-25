
import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';

function OfficeList() {

    const API_URL = "http://localhost:8082/api/office";

    const [offices, setOffices] = useState([]);
    const [search, setSearch] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const loadOffices = useCallback(async () => {
        try {
            setLoading(true);

            const response = await axios.get(`${API_URL}/all`);

            setOffices(response.data);
            setMessage("");

        } catch (error) {
            setMessage("Unable to load offices.");
            console.error(error);

        } finally {
            setLoading(false);
        }
    }, []);


    useEffect(() => {
        loadOffices();
    }, [loadOffices]);


    const filteredOffices = offices.filter((office) =>
        office.name.toLowerCase().includes(search.toLowerCase()) ||
        String(office.officeNumber).includes(search)
    );


    const handleDelete = async (id) => {

        const confirmed = window.confirm(
            "Are you sure you want to delete this office?"
        );

        if (!confirmed) {
            return;
        }

        try {
            const response = await axios.delete(
                `${API_URL}/delete/${id}`
            );

            setMessage(response.data);

            await loadOffices();

        } catch (error) {
            const errorData = error.response?.data;

            setMessage(
                typeof errorData === "string"
                    ? errorData
                    : errorData?.message || "Unable to delete office."
            );
        }
    };

    return (
        <div>

            <h1>Available Offices</h1>

            {/* Search field */}
            <input
                type="text"
                placeholder="Search by office name or number"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
            />

            <br /><br />

            {message && <p>{message}</p>}

            {loading ? (
                <p>Loading offices...</p>
            ) : (

                <table border="1" cellPadding="10">

                    <thead>
                        <tr>
                            <th>Office Name</th>
                            <th>Office Number</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>

                        {filteredOffices.length > 0 ? (

                            filteredOffices.map((office) => (

                                <tr key={office.id}>

                                    <td>{office.name}</td>

                                    <td>{office.officeNumber}</td>

                                    <td>
                                        <button
                                            type="button"
                                            onClick={() =>
                                                handleDelete(office.id)
                                            }
                                        >
                                            Delete
                                        </button>

                                        {" "}

                                        <button
                                            type="button"
                                            disabled
                                            title="Update will be implemented in the next class"
                                        >
                                            Update
                                        </button>
                                    </td>

                                </tr>

                            ))

                        ) : (

                            <tr>
                                <td colSpan="3">
                                    No offices found.
                                </td>
                            </tr>

                        )}

                    </tbody>

                </table>

            )}

        </div>
    );
}

export default OfficeList;
