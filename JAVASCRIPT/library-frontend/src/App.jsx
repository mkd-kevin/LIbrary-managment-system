
import './App.css';

import { Routes, Route } from 'react-router-dom';

import LibraryButton from './Components/LibraryButton';
import LibraryFooter from './Components/LibraryFooter';
import LibraryHeader from './Components/LibraryHeader';
import LibraryBooks from './Components/LibraryBooks';
import LibraryLogin from './Components/Librarylogin';

import Office from './Components/Office';
import OfficeList from './Components/OfficeList';
import LibraryCards from './Components/Librarycards';

function HomePage() {
    return (
        <div>
            <LibraryHeader />

            <main> 
              <h1>Doctor Management</h1>
              <p>This is the doctor dashboard</p>
              {/*
                <LibraryBooks />
                <LibraryButton />

                <div>
                    Check books
                </div> */}
            </main>

           {/*
            <LibraryFooter />
           */}
        </div>
    );
}
  
function App() {
    return (
        <Routes>
            <Route path="/" element={<HomePage />} />

            <Route path="/login" element={<LibraryLogin />} />

            <Route path="/office" element={<Office />} />

            <Route path="/offices" element={<OfficeList />} />

            <Route path="/books" element={<LibraryBooks />} />

            <Route path="*" element={<HomePage />} />
        </Routes>
    );
}

export default App;
