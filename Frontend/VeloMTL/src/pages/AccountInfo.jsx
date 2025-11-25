import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from "../context/AuthContext";  // Import your AuthContext for managing user state
import axios from 'axios'; // Import axios for HTTP requests

const AccountInfo = () => {
    const { user, activeRole, toggleRole } = useContext(AuthContext);  // Use context to get user info and active role
    const [flexDollars, setFlexDollars] = useState(0);  // Store Flex Dollars balance
    const [loading, setLoading] = useState(true);  // Loading state for fetching data

    const token = localStorage.getItem('token');  // Assuming token is saved in localStorage

    // Fetch Flex Dollars from the backend
    useEffect(() => {
        axios.get('/api/riders/flex-dollars', {
            headers: {
                Authorization: `Bearer ${token}`,  // Send the token for authentication
            }
        })
        .then(response => {
            // Assuming the backend sends a field named 'flexDollars'
            setFlexDollars(response.data.flexDollars);  // Set the Flex Dollars to state
            setLoading(false);  // Stop loading once data is fetched
        })
        .catch(error => {
            console.error('Error fetching flex dollars:', error);
            setLoading(false);  // Stop loading on error
        });
    }, [token]);

    // Toggle the role between 'rider' and 'operator'
    const handleRoleToggle = () => {
        toggleRole();  // Call the toggleRole function from context to switch roles
    };

    if (loading) {
        return <div>Loading...</div>;  // Show loading state while fetching data
    }

    return (
        <div className="account-info-container">
            <h2>Account Information</h2>
            <div className="flex-dollars">
                <p><strong>Flex Dollars:</strong> {flexDollars}</p>  {/* Displaying Flex Dollars */}
            </div>
            <div className="role-toggle">
                <button onClick={handleRoleToggle}>
                    Switch to {activeRole === 'RIDER' ? 'Operator' : 'Rider'} View
                </button>
            </div>
        </div>
    );
};

export default AccountInfo;
