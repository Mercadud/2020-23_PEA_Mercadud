const
    express = require("express"),
    app = express(),
    fs = require('fs-extra').promises,
    path = require('path'),
    PORT = 8888,
    WEBROOT = path.join(__dirname, "./client/public"),
    DATA = path.join(__dirname, "data");

let movieJ;
// change this to false if you want to use the movie file
false ? movieJ = "IMDBmovieDataForTesting.json" : movieJ = "IMDBmovieData.json";

app.use(express.static(WEBROOT));
app.use(express.json());

app.route('/movies')
    .get((_, res) => {
        findMovies(res, (movie) => {
            return {
                "Title": movie.Title,
                "Key": movie.Key,
                "Year": movie.Year,
                "Runtime": movie.Runtime,
                "Revenue": movie.Revenue
            }
        });
    })
    .post((req, res) => {
        try {
            fs.readFile(path.join(DATA, movieJ), {
                "encoding": "utf-8"
            }).then(el => JSON.parse(el))
                .then(movies => {
                    console.log(req.body);
                    let biggestKey = 0;
                    movies.map(movie => {
                        if (movie.Key > biggestKey)
                            biggestKey = movie.Key;
                    });
                    movies.push({
                        "Key": biggestKey + 1,
                        "Title": req.body.Title,
                        "Genre": req.body.Genre,
                        "Actors": req.body.Actors,
                        "Year": req.body.Year,
                        "Runtime": req.body.Runtime,
                        "Revenue": req.body.Revenue
                    });
                    fs.writeFile(path.join(DATA, movieJ), JSON.stringify(movies, null, '\t'))
                        .then(_ => {
                            res.send(true);
                        })
                        .catch(r => {
                            console.error(r);
                            res.send(false);
                        });
                });
        }
        catch {
            res.send(false);
        }

    })

app.get("/movies/:id", (req, res) => {
    findMovies(res, (movie) => {
        if (movie.Key == req.params.id) {
            return movie;
        }
    })
});

app.get('/actors/:name', (req, res) => {
    findMovies(res, (movie) => {
        let actorFound = false;
        movie.Actors.map(actor => {
            if (actor.includes(req.params.name)) {
                actorFound = true;
            }
        });
        if (actorFound) {
            return {
                "Title": movie.Title,
                "Key": movie.Key,
                "Year": movie.Year,
                "Runtime": movie.Runtime,
                "Revenue": movie.Revenue
            };
        }
    });
});

app.get('/years/:year', (req, res) => {
    console.log(req.params.year);
    findMovies(res, (movie) => {
        if (movie.Year == req.params.year) {
            return {
                "Title": movie.Title,
                "Key": movie.Key,
                "Runtime": movie.Runtime,
                "Revenue": movie.Revenue
            };
        }
    })
})

function findMovies(res, cb) {
    let movieList = [];
    fs.readFile(path.join(DATA, movieJ), {
        encoding: "utf-8",
    }).then(el => JSON.parse(el))
        .then(movies => {
            movies.map(movie => {
                let t = cb(movie);
                if (t) movieList.push(t);
            });
            movieList.sort((a, b) => a.Title.toString().localeCompare(b.Title.toString()));
            res.send(movieList);
        })
        .catch(err => {
            console.error(`ERR: ${err}`);
        });
}

app.listen(PORT, () => {
    console.log(`listening on port : ${PORT}`);
});
