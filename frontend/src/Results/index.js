import React, { useEffect, useState } from "react";
import doFetch from "../Util/doFetch";
import { useLocalStorage } from "../Util/useLocalStorage";
import { Button, Col, Container, Table } from "react-bootstrap";
import { FaCheck, FaTimes } from "react-icons/fa";

const Results = () => {
  const taskId = window.location.href.split("/tasks/results/")[1].split("/")[0];
  const userId = window.location.href.split("/tasks/results/")[1].split("/")[1];

  const [jwt, setJwt] = useLocalStorage("", "jwt");

  const [solution, setSolution] = useState({});
  const [listOfResults, setListOfResults] = useState([]);

  const [comments, setComments] = useState([]);
  const [comment, setComment] = useState({
    userId: userId,
    taskId: taskId,
    text: "",
  });

  const [upd, setUpd] = useState(0);

  function updateStatus(status) {
    const statusDTO = {
      value: status,
    };
    doFetch(
      `/api/tasks/${taskId}/${userId}/update-status`,
      "put",
      jwt,
      statusDTO
    ).then((response) => {});
  }

  useEffect(() => {
    doFetch(`/api/solutions/${userId}/${taskId}`, "get", jwt).then(
      (response) => {
        setSolution(response);
      }
    );
  }, []);

  useEffect(() => {
    doFetch(`/api/verification/${taskId}/${userId}`, "get", jwt).then(
      (response) => {
        setListOfResults(response);
      }
    );
  }, []);

  useEffect(() => {
    console.log("fetching !")
    doFetch(`/api/comments/${taskId}/${userId}`, "get", jwt).then(
      (response) => {
        setComments(response);
      }
    );
  }, [upd]);

  function updateComment(value) {
    const commentCopy = { ...comment };
    commentCopy.text = value;
    setComment(commentCopy);
  }

  function postComment() {
    console.log("comment: ", comment);
    doFetch(`/api/comments`, "post", jwt, comment).then((response) => {
      const commentsCopy = [...comments];
      commentsCopy.push(comment);
      setComments(commentsCopy);
    });
  }

  return (
    <Container className="mt-5">
      <div className="mt-4">
        <Col sm="9" md="8" lg="9">
          <h1>Solution: </h1>
          <textarea
            style={{ width: "100%", borderRadius: "0.5em", height: "200px" }}
            value={solution.text}
          ></textarea>
        </Col>
      </div>

      {listOfResults[0] ? (
        <div className="mt-4">
          <h2>Results</h2>
          <Table striped bordered>
            <thead>
              <tr>
                <th>Received</th>
                <th>Expected</th>
                <th>Result</th>
              </tr>
            </thead>
            <tbody>
              {listOfResults.map((result, index) => (
                <tr key={index}>
                  <td>{result.received}</td>
                  <td>{result.expected}</td>
                  <td>
                    {result.result ? (
                      <FaCheck className="text-success" />
                    ) : (
                      <FaTimes className="text-danger" />
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      ) : (
        <></>
      )}

      <div className="mt-4">
        <textarea
          style={{ width: "100%", borderRadius: "1em", height: "100px" }}
          onChange={(event) => updateComment(event.target.value)}
        ></textarea>
        <Button
          variant="btn btn-outline-secondary"
          onClick={() => {
            postComment();
            setUpd(upd + 1);
          }}
        >
          Post Comment
        </Button>
      </div>

      <div className="mt-4">
        <h4>My comments:</h4>
        {comments.map((comment) => (
          <div key={comment.id} className="card mt-2">
            <div className="card-body">
              <p className="card-text">{comment.text}</p>
              <p className="card-subtitle mb-2 text-muted">
                Posted:{" "}
                {new Date(comment.createdDate).toLocaleString("en-GB", {
                  month: "long",
                  day: "numeric",
                  hour: "numeric",
                  minute: "numeric",
                })}
              </p>
            </div>
          </div>
        ))}
      </div>

      <div className="d-flex justify-content-center">
        <Button
          className="mt-4 mx-2"
          variant="btn btn-outline-secondary"
          onClick={() => {
            updateStatus("COMPLETED");
          }}
        >
          Complete
        </Button>
        <Button
          className="mt-4 mx-2"
          variant="btn btn-outline-secondary"
          onClick={() => {
            updateStatus("REJECTED");
          }}
        >
          Reject
        </Button>
      </div>
    </Container>
  );
};

export default Results;
