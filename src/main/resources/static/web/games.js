fetch( "/api/games").then(function(response) {
  if (response.ok) {
    return response.json();
  }
  throw new Error(response.statusText);
}).then(function(json) {
  // do something with the JSON
  // note that this does not add a new promise
  console.log(json);   //
}).catch(function(error) {
  console.log( "Request failed: " + error.message );
});