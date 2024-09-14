import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
    const token = localStorage.getItem('token');  // Check if JWT token exists

    if (!token) {
        return <Navigate to="/login" />;  // If no token, redirect to login page
    }

    return children; 
};

export default ProtectedRoute;
