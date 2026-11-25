
import { useState } from 'react';
import axios from 'axios';

function Office() {

    const API_URL = "http://localhost:8082/api/office";

    const [name, setName] = useState("");
    const [officeNumber, setOfficeNumber] = useState("");
    const [deleteOfficeNumber, setDeleteOfficeNumber] = useState("");
    const [message, setMessage] = useState("");

    // Save Office
    const handleSave = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post(`${API_URL}/save`, {
                name: name,
                officeNumber: Number(officeNumber)
            });

            setMessage(response.data);

            setName("");
            setOfficeNumber("");

        } catch (error) {
            setMessage(
                error.response?.data || "Unable to save office."
            );
        }
    };

    // Delete Office using Office Number
    const handleDelete = async (e) => {
        e.preventDefault();

        try {
            // Get all offices
            const response = await axios.get(`${API_URL}/all`);

            // Find office using its office number
            const office = response.data.find(
                (item) =>
                    String(item.officeNumber) === String(deleteOfficeNumber)
            );

            // Check if office exists
            if (!office) {
                setMessage("Office not found.");
                return;
            }

            // Delete office using its UUID
            const result = await axios.delete(
                `${API_URL}/delete/${office.id}`
            );

            setMessage(result.data);
            setDeleteOfficeNumber("");

        } catch (error) {
            setMessage(
                error.response?.data || "Unable to delete office."
            );
        }
    };

    return (
        <div>

            <h1>Save Office</h1>

            <form onSubmit={handleSave}>

                <label>Office Name:</label>

                <input
                    type="text"
                    placeholder="Enter office name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />

                <br /><br />

                <label>Office Number:</label>

                <input
                    type="number"
                    placeholder="Enter office number"
                    value={officeNumber}
                    onChange={(e) => setOfficeNumber(e.target.value)}
                    min="1"
                    step="1"
                    required
                />

                <br /><br />

                <button type="submit">
                    SAVE OFFICE
                </button>

            </form>

            <br /><br /><br />

            <h1>Delete Office</h1>

            <form onSubmit={handleDelete}>

                <label>Office Number:</label>

                <input
                    type="number"
                    placeholder="Enter office number to delete"
                    value={deleteOfficeNumber}
                    onChange={(e) =>
                        setDeleteOfficeNumber(e.target.value)
                    }
                    min="1"
                    step="1"
                    required
                />

                <br /><br />

                <button type="submit">
                    DELETE OFFICE
                </button>

            </form>

            {message && (
                <p style={{ textAlign: "center" }}>
                    {message}
                </p>
            )}

        </div>
    );
}

export default Office;
