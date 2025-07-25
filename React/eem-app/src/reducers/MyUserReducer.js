import cookie from 'react-cookies';

const myUserReducer = (current, action) => {
    switch (action.type) {
        case "login":
            return action.payload;
        case "logout":
            cookie.remove('token');
            return null;
        default:
            return current;
    }
};

export default myUserReducer;