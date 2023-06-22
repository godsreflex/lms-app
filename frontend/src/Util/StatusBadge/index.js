import React from 'react';
import { Badge } from 'react-bootstrap';

const StatusBadge = (props) => {
    const { text } = props;
  
    function getColorOfBagde() {
      if (text === "COMPLETED") {
        return "success";
      }
      if (text === "AWAITING") {
        return "warning";
      }
      if (text === "REJECTED") {
        return "danger";
      }
      return "secondary";
    }
  
    return (
      <Badge pill bg={getColorOfBagde()} style={{ fontSize: "0.8em" }}>
        {text}
      </Badge>
    );
  };

export default StatusBadge;