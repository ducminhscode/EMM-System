import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import Login from "./components/Login";
import Home from "./components/Home";
import { Container } from "react-bootstrap";
import { MyUserContext } from "./configs/Contexts";
import { MyDispatchContext } from "./configs/Contexts";
import { useReducer, useState, useEffect } from "react";
import MyUserReducer from "./reducers/MyUserReducer";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import cookie from 'react-cookies';
import { authApis, endpoints } from "./configs/Apis";
import Profile from "./components/Profile";
import ChatBox from "./components/ChatBox";
import ProblemTechnician from "./components/ProblemTechnician";
import ProblemTechnicianList from "./components/ProblemTechnicianList";
<<<<<<< HEAD
import DeviceList from "./components/DeviceList";
=======
import MaintenanceTechnicianList from "./components/MaintenanceTechnicianList";
import MaintenanceTechnician from "./components/MaintenanceTechnician";
>>>>>>> 8bb2270cbfe47cca34b5260b5172306d22bb585d

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = cookie.load('token');
    if (token) {
      authApis().get(endpoints['profile'])
        .then(res => {
          dispatch({
            type: 'login',
            payload: res.data,
          });
        })
        .catch(err => {
          console.error('Failed to load user data:', err);
          cookie.remove('token');
        })
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, []);

  if (loading) {
    return <div></div>;
  }

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
        <BrowserRouter>
          <Container>
            <Routes>
              <Route path="/login" element={user ? <Navigate to="/" /> : <Login />} />

              <Route
                path="*"
                element={
                  <>
                    {user && <Header />}
                    <div>
                      <Container>
                        <Routes>
                          <Route path="/" element={user ? <Home /> : <Navigate to="/login" />} />
                          <Route path="/profile" element={user ? <Profile /> : <Navigate to="/login" />} />
                          <Route path="/chat" element={user ? <ChatBox /> : <Navigate to="/login" />} />
                          <Route path="/problem-technician-list" element={user ? <ProblemTechnicianList /> : <Navigate to="/login" />} />
<<<<<<< HEAD
                          <Route path="/problem-technician" element={user ? <ProblemTechnician /> : <Navigate to="/login" />} />
                          <Route path="/devices" element={user ? <DeviceList /> : <Navigate to="/login" />} />
=======
                          <Route path="/problem-technician/:problemId" element={user ? <ProblemTechnician /> : <Navigate to="/login" />} />
                          <Route path="/maintenance-technician-list" element={user ? <MaintenanceTechnicianList /> : <Navigate to="/login" />} />
                          <Route path="/maintenance-technician/:maintenanceId" element={user ? <MaintenanceTechnician /> : <Navigate to="/login" />} />
>>>>>>> 8bb2270cbfe47cca34b5260b5172306d22bb585d
                        </Routes>
                      </Container>
                    </div>
                    {user && <Footer />}
                  </>
                }
              />
            </Routes>
          </Container>
        </BrowserRouter>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>
  );
}

export default App;