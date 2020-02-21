function getToken() {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: "http://qanswer-core1.univ-st-etienne.fr/api/user/signin",
      type: "POST",
      contentType: "application/json",
      headers: {
        "Content-Type": "application/json"
      },
      data: JSON.stringify({
        usernameOrEmail: "QNico",
        password: "J*M'isuMdR*20"
      }),
      crossDomain: true,

      success: function(data) {
        resolve(data);
      },
      error: function(error) {
        reject(error);
      }
    });
  });
}

function qA(token, question, lang) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url:
        "http://qanswer-core1.univ-st-etienne.fr/api/qa/fullInterpretation?question=" +
        question +
        "?&lang=" +
        lang,
      type: "GET",
      contentType: "application/json",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token
      },
      crossDomain: true,

      success: function(data) {
        resolve(data);
      },
      error: function(error) {
        reject(error);
      }
    });
  });
}

getToken().then(function(value) {
  qA(value["accessToken"], "code postal Bordeaux", "fr").then(value => {
    // value is response != value of token
    //console.log(value['interpretation'])
    document.body.innerHTML =
      "Question =       ".toUpperCase().bold() +
      value["question"] +
      "<br>" +
      "Interpretation = ".toUpperCase().bold() +
      value["interpretation"] +
      "<br>" +
      "Contexte =       ".toUpperCase().bold();
    value["qaContexts"].forEach(a => {
      document.body.innerHTML += a.literal + " | ";
    });

    //  response.qaContexts[0].literal
  });
});
//console.log(token)
