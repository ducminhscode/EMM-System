import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import Login from "./components/Login";
import Home from "./components/Home";
import { Container } from "react-bootstrap";
import { MyUserContext } from "./configs/Contexts";
import { MyDispatchContext } from "./configs/Contexts";
import { useReducer } from "react";
import MyUserReducer from "./reducers/MyUserReducer";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);



  return (
    <MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
        <BrowserRouter>

          <Container>
            <Routes>
              <Route path="/login" element={<Login />} />

              <Route
                path="*"
                element={
                  <>
                    <Header />
                    <div style={{ paddingTop: "70px" }}>
                      <Container>
                        <Routes>
                          <Route path="/" element={user ? <Home /> : <Navigate to="/login" />} />
                        </Routes>
                      </Container>
                    </div>
                    <Footer />
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