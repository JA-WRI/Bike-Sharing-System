import React from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav style={{ padding: '1rem', background: '#333', color: '#fff' }}>
      <Link to="/" style={{ marginRight: '1rem', color: '#fff', textDecoration: 'none' }}>
        Home
      </Link>
      <Link to="/login" style={{ marginRight: '1rem', color: '#fff', textDecoration: 'none' }}>
        Login
      </Link>
      <Link to="/register" style={{ color: '#fff', textDecoration: 'none' }}>
        Register
      </Link>
    </nav>
  );
};

export default Navbar;