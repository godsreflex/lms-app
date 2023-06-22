import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

import { Navigate, Route, Routes, parsePath } from "react-router-dom";
import Login from "./Login";
import StudentDashboard from "./StudentDashboard";
import jwt_decode from "jwt-decode";
import { useLocalStorage } from "./Util/useLocalStorage";
import { useState } from "react";
import PrivateRoute from "./Util/PrivateRoute";
import TeacherDashboard from "./TeacherDashboard";
import TeacherTask from "./TeacherTask";
import PrivateTaskPageRoute from "./Util/PrivateTaskPageRoute";
import StudentTask from "./StudentTask";
import Tests from "./Tests";
import Results from "./Results";

function App() {
  const [jwt, setJwt] = useLocalStorage("", "jwt");
  const [role, setRole] = useState("");

  function getRole() {
    if (jwt) return jwt_decode(jwt).role;
    return "";
  }

  return (
    <Routes>
      <Route path="login" element={<Login />} />

      <Route
        path="dashboard"
        element={
          getRole() === "ROLE_STUDENT" ? (
            <PrivateRoute>
              <StudentDashboard />
            </PrivateRoute>
          ) : (
            <PrivateRoute>
              <TeacherDashboard />
            </PrivateRoute>
          )
        }
      />

      <Route
        path="tasks/:id"
        element={
          getRole() === "ROLE_TEACHER" ? (
            <PrivateRoute>
              <TeacherTask />
            </PrivateRoute>
          ) : getRole() === "ROLE_STUDENT" ? (
            <PrivateRoute>
              <PrivateTaskPageRoute
                taskId={window.location.href.split("/tasks/")[1]}
              >
                <StudentTask />
              </PrivateTaskPageRoute>
            </PrivateRoute>
          ) : (
            <></>
          )
        }
      />

      <Route
        path="tasks/tests/:id"
        element={
          getRole() === "ROLE_TEACHER" ? (
            <PrivateRoute>
              <Tests />
            </PrivateRoute>
          ) : (
            <PrivateRoute>
              <StudentDashboard />
            </PrivateRoute>
          )
        }
      />

      <Route
        path="tasks/results/:task_id/:user_id"
        element={
          getRole() === "ROLE_TEACHER" ? (
            <PrivateRoute>
              <Results />
            </PrivateRoute>
          ) : (
            <PrivateRoute>
              <StudentDashboard />
            </PrivateRoute>
          )
        }
      />
    </Routes>
  );
}

export default App;
