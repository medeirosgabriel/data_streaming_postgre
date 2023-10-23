import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 0 },
        { duration: '10s', target: 8000 },
        { duration: '10m', target: 8000 },
        { duration: '10s', target: 0 },
    ],
};

export default function () {
    http.post('http://localhost:8094/api/order');
    sleep(1);
}
