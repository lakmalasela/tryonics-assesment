import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import LoginForm from './views/LoginForm';
import Dashboard from './views/Dashboard';
import ProtectedRoute from './components/ProtectedRoute'; 
import CreateUser from './views/CreateUser';
import Promotion from './views/Promotion';

// Function to check if the user is authenticated
const isAuthenticated = () => {
    // Example: Check if a JWT token is present in local storage
    return localStorage.getItem('token') !== null;
};

// Component to handle redirect logic
const AuthRedirect = () => {
    const navigate = useNavigate();

    useEffect(() => {
        if (isAuthenticated()) {
            navigate('/dashboard');
        } else {
            navigate('/login');
        }
    }, [navigate]);

    return null; // This component doesn't render anything
};

function App() {
    return (
        <Router>
            <Routes>
                {/* Redirect to login or dashboard based on authentication */}
                <Route path="/" element={<AuthRedirect />} />

                {/* Login Route */}
                <Route path="/login" element={<LoginForm />} />

                {/* Protected Dashboard Route */}
                <Route 
                    path="/dashboard" 
                    element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    } 
                />

                {/* Create Admin Route */}
                <Route 
                    path="/createuser" 
                    element={
                        <ProtectedRoute>
                            <CreateUser />
                        </ProtectedRoute>
                    } 
                />

                  {/* Create Admin Route */}
                  <Route 
                    path="/promotion" 
                    element={
                        <ProtectedRoute>
                            <Promotion />
                        </ProtectedRoute>
                    } 
                />
            </Routes>
        </Router>
    );
}

export default App;
