import React, { useContext, useState, useEffect } from 'react';
import { AuthContext } from '../context/AuthContext';  // Import the AuthContext
import axios from 'axios'; // Ensure axios is imported for the API call

const AccountInfo = () => {
  const { user, activeRole, toggleRole } = useContext(AuthContext);  // Use context to get user and activeRole
  const [flexDollars, setFlexDollars] = useState(0);  // Store Flex Dollars balance
  const [loading, setLoading] = useState(true);  // Loading state for fetching data

  const token = localStorage.getItem('token');  // Assuming token is saved in localStorage

  useEffect(() => {
    if (token) {
      axios.get('/api/riders/flex-dollars', {
        headers: {
          Authorization: `Bearer ${token}`,  // Send the token for authentication
        }
      })
      .then(response => {
        setFlexDollars(response.data.flexDollars);  // Set the Flex Dollars to state
        setLoading(false);  // Stop loading once data is fetched
      })
      .catch(error => {
        console.error('Error fetching flex dollars:', error);
        setLoading(false);  // Stop loading on error
      });
    } else {
      console.error('Token not found in localStorage');
      setLoading(false);
    }
  }, [token]);

  if (loading) {
    return <div>Loading...</div>;  // Show loading state while fetching data
  }

  return (
    <div className="account-info-container">
      <h2>Account Information</h2>

      {activeRole === "RIDER" && (
        <div className="rider-info">
          <p>Welcome, Rider {user?.name}</p>
          <div className="flex-dollars">
            <p><strong>Flex Dollars:</strong> {flexDollars}</p> {/* Display Flex Dollars for Rider */}
          </div>
        </div>
      )}

      {activeRole === "OPERATOR" && (
        <div className="operator-info">
          <p>Welcome, Operator {user?.name}</p>  {/* Display Operator's name */}
          {/* Add any operator-specific information here */}
        </div>
      )}

      <div className="role-toggle">
        <button onClick={toggleRole}>
          Switch to {activeRole === 'RIDER' ? 'Operator' : 'Rider'} View
        </button>
      </div>
    </div>
  );
};

export default AccountInfo;
