
import { useState } from 'react';
import { Link } from 'react-router-dom';

function LibraryLogin() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleLogin = (e) => {
        e.preventDefault();

        if (!username || !password) {
            setMessage('Please enter your username and password.');
            return;
        }

        setMessage('Login is not connected to the backend yet.');
    };

    const handleCreateAccount = () => {
        setMessage('Account registration is not available yet.');
    };

    return (
        <div className="login-page">

            {/* Navigation */}
            <nav className="login-navigation">
                <Link to="/login">Login here</Link>
                {' | '}
                <Link to="/office">Office Management</Link>
                {' | '}
                <Link to="/offices">View All Offices</Link>
                {' | '}
                <Link to="/books">View Books</Link>
            </nav>

            {/* Login Form */}
            <div className="login-container">

                <form onSubmit={handleLogin}>

                    <div className="login-field">
                        <label>UserName:</label>
                        <input
                            type="text"
                            placeholder="Enter Your username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    <div className="login-field">
                        <label>Password:</label>
                        <input
                            type="password"
                            placeholder="Enter Your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button type="submit">
                        LOGIN
                    </button>

                </form>

                <div className="create-account">

                    <p>New here?</p>

                    <button
                        type="button"
                        onClick={handleCreateAccount}
                    >
                        Create Account
                    </button>

                </div>

                {message && (
                    <p className="login-message">
                        {message}
                    </p>
                )}

            </div>

        </div>
    );
}

export default LibraryLogin;
