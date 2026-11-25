
import { Link } from 'react-router-dom';

function LibraryButton() {
    return (
        <Link to="/login">
            <button type="button">
                Login
            </button>
        </Link>
    );
}

export default LibraryButton;
