let chai = require('chai');
let server = require('../app');

let chaiHttp = require('chai-http');
chai.use(chaiHttp);
chai.should();

//Test for query search that checks if a array of bikes is returned
describe('/GET search', () => {

    it('it should GET all the bikes searched', (done) => {
        chai.request(server)
            .get("/api/search/trek?num_items=6&offset=0")
            .end((err, res) => {
                res.should.have.status(200);
                let resObj = JSON.parse(res.text);
                resObj.data.should.be.a('array');
                resObj.data.length.should.not.be.eq(0);
                done();
            });
    });

});

//Test for query count numbers checks if a count of bikes is returned
describe('/GET count number', () => {

    it('it should GET all the bikes count', (done) => {
        chai.request(server)
            .get("/api/search/trek?num_items=6&offset=0")
            .end((err, res) => {
                res.should.have.status(200);
                let resObj = JSON.parse(res.text);
                let count = resObj.count;
                count.should.not.be.eq(0);
                done();
            });
    });

});

//Test for query bike model checks if an array of models is returned
describe('/GET bike model', () => {

    it('it should GET bike model', (done) => {
        chai.request(server)
            .get("/api/bike-model/marlin")
            .end((err, res) => {
                res.should.have.status(200);
                let resObj = JSON.parse(res.text);
                resObj.should.be.a('array');
                resObj.length.should.not.be.eq(0);
                done();
            });
    });
});

//Test for query bike instance checks if an array of instances is returned
describe('/GET bike instance', () => {

    it('it should GET bike instance', (done) => {
        chai.request(server)
            .get("/api/bike-instance/1")
            .end((err, res) => {
                res.should.have.status(200);
                let resObj = JSON.parse(res.text);
                resObj.should.be.a('array');
                resObj.length.should.not.be.eq(0);
                done();
            });
    });
});

//Test for query bike model comparison if an array of comparisons is returned
describe('/GET bike comparison', () => {

    it('it should GET bike comparison', (done) => {
        chai.request(server)
            .get("/api/bike-comparison/1")
            .end((err, res) => {
                res.should.have.status(200);
                let resObj = JSON.parse(res.text);
                resObj.should.be.a('array');
                resObj.length.should.not.be.eq(0);
                done();
            });
    });
});




