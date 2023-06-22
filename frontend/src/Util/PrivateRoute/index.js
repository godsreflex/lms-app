import React from "react";
import { Navigate } from "react-router-dom";
import { useLocalStorage } from "../useLocalStorage";
import jwt_decode from "jwt-decode";
import doFetch from "../doFetch";

const PrivateRoute = ({ children }) => {
  const [jwt, setJwt] = useLocalStorage("", "jwt");
  const [refresh, setRefresh] = useLocalStorage("", "refresh");

  if (jwt) {
    const currentTime = new Date().getTime();
    console.log("current time: ", currentTime);

    const expTime = jwt_decode(jwt).exp * 1000;
    console.log("exp time: ", expTime);

    const refreshToken = {
      value: refresh,
    };

    if (expTime < currentTime) {
      doFetch("api/auth/refresh", "post", null, refreshToken).then(
        (response) => {
          console.log("response: ", response);
          setJwt(response.accessToken);
        }
      );
    }
  }

  return jwt ? children : <Navigate to="/login" />;
};

export default PrivateRoute;
