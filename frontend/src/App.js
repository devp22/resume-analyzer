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
    const formData = new FormData();
    formData.append("file", file[0]);
    axios.post("http://localhost:8080/api/file", file[0].name, {});
    axios.post("http://localhost:8080/api/upload", formData, {});
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
