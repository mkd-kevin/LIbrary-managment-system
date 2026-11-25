import React from 'react';
import { Link } from 'react-router-dom';

function LibraryHeader() {
  return (
    <div>
      <header>
        <h1>Library System</h1>

        <p>
          This is library system that will help you to manage the books
        </p>

        <nav>
          <Link to="/office">Office Management</Link>
          {' | '}
          <Link to="/offices">View All Offices</Link>
          {' | '}
          <Link to="/books">View All Books</Link>
          {' | '}
          <Link to="/">Doctor Management</Link>
        </nav>
      </header>
    </div>
  );
}

export default LibraryHeader;