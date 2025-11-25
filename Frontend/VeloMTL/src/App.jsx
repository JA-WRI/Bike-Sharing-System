import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './index.css';

import PaymentPlans from "./pages/PaymentPlans";
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import Register from './pages/Register';
import AddPayment from "./pages/AddPayment";
import OauthRedirect from "./pages/OauthRedirect";
import BillingPage from "./pages/BillingPage";
import History from "./pages/History";
import AccountInfo from "./pages/AccountInfo"; 


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
        <Route path="/history" element={<History />} />
        <Route path="/oauth2/redirect" element={<OauthRedirect />} />
        {/* Account Info route visible only for riders */}
        <Route path="/account-info" element={<AccountInfo />} />
      </Routes>
    </Router>
  );
};

export default App;
