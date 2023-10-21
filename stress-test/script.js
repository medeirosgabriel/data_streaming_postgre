import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 0 },
        { duration: '10m', target: 100 },
        { duration: '10s', target: 0 },
    ],
};

export default function () {
    http.post('http://localhost:8081/api/order');
    sleep(5);
}