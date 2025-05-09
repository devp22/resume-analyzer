import logo from "./logo.svg";
import "./App.css";
import React, { useState } from "react";
import axios from "axios";
import { FileUploader } from "react-drag-drop-files";

const fileTypes = ["JPG", "PNG", "GIF", "PDF"];

function App() {
  const [file, setFile] = useState(null);
  const handleChange = (file) => {
    setFile(file);
    axios.post("http://localhost:8080/api/file", file[0].name, {
      headers: { "Content-Type": "application/json" },
    });
  };
  return (
    <div className="App">
      <FileUploader
        handleChange={handleChange}
        multiple={true}
        name="file"
        types={fileTypes}
      />
      <p>{file ? `File name: ${file[0].type}` : "no files uploaded yet"}</p>
    </div>
  );
}

export default App;
