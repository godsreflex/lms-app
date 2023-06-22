import React, { useEffect, useState } from "react";
import { useLocalStorage } from "../useLocalStorage";
import doFetch from "../doFetch";
import jwt_decode from "jwt-decode";
import { Navigate } from "react-router-dom";

const PrivateTaskPageRoute = ({ taskId, children }) => {
  const [jwt, setJwt] = useLocalStorage("", "jwt");
  const [isTaskOwner, SetIsTaskOwner] = useState({
    value: false,
  });

  function getUserId() {
    return jwt_decode(jwt).id;
  }

  useEffect(() => {
    doFetch(
      `/api/users/${getUserId()}/is-task-owner/${taskId}`,
      "get",
      jwt
    ).then((isTaskOwnerResponse) => {
      SetIsTaskOwner(isTaskOwnerResponse.value);
    });
  });

  return isTaskOwner ? children : <Navigate to="/dashboard" />;
};

export default PrivateTaskPageRoute;
