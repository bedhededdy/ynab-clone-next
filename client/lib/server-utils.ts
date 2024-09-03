import { cookies } from "next/headers";

export function serverFetch(url: URL | string, options?: RequestInit): Promise<Response> {
  // FIXME: DON'T HARD-CODE THE URL
  url = "http://localhost:8080" + url.toString();
  return fetch(url, {
    credentials: "include",
    ...options,
    headers: {
      "Content-Type": "application/json",
      "Accept": "application/json",
      "Cookie": cookies().toString(),
      ...options?.headers,
    }
  });
}
