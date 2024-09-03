/** @type {import('next').NextConfig} */
const nextConfig = {
    async rewrites() {
        return [{
            source: "/api/:path*",
            destination: "http://localhost:8080/api/:path*",
        }];
    },

    async headers() {
        return [{
            source: "/api/:path*",
            headers: [
                { key: "Credentials", value: "include" },
                { key: "Content-Type", value: "application/json" },
                { key: "Accept", value: "application/json" }
            ]
        }]
    }
};

export default nextConfig;
