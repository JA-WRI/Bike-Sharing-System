import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './index.css';

import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import Register from './pages/Register';
import OauthRedirect from "./pages/OauthRedirect";
import AddPayment from "./pages/AddPayment";
import BillingPage from "./pages/BillingPage";
import History from './pages/History';

const App = () => {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/add-payment" element={<AddPayment />} />
        <Route path="/payment-plans" element={<PaymentPlans />} />
        <Route path="/billing" element={<BillingPage />} />
        <Route path="/History" element={<History />} />
        <Route path="/oauth2/redirect" element={<OauthRedirect />} />
      </Routes>
    </Router>
  );
};

export default App;
