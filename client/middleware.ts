import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { cookies } from "next/headers";

const protectedRoutes = ["/budget-select"];
const publicRoutes = ["/login", "/signup", "/forgot-password", "/"];

export function middleware(request: NextRequest) {
  console.log("Middleware invoked on: ", request.nextUrl.pathname);

  // We assume the jwt in the cookie is valid.
  // We will rely on a failed auth in the page we try to access if the
  // jwt is invalid.
  const cookiesStore = cookies();
  const jwt = cookiesStore.get("jwt") || "";

  if (request.nextUrl.pathname === "/login" && jwt)
    return NextResponse.redirect(new URL("/budget-select", request.nextUrl.origin));

  if (protectedRoutes.includes(request.nextUrl.pathname) && !jwt)
    return NextResponse.redirect(new URL("/login", request.nextUrl.origin));

  return NextResponse.next();
}

export const config = {
  matcher: [
    /*
     * Match all request paths except for the ones starting with:
     * - api (API routes)
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico, sitemap.xml, robots.txt (metadata files)
     */
    {
      source:
        '/((?!api|_next/static|_next/image|favicon.ico|sitemap.xml|robots.txt).*)',
      missing: [
        { type: 'header', key: 'next-router-prefetch' },
        { type: 'header', key: 'purpose', value: 'prefetch' },
      ],
    },

    {
      source:
        '/((?!api|_next/static|_next/image|favicon.ico|sitemap.xml|robots.txt).*)',
      has: [
        { type: 'header', key: 'next-router-prefetch' },
        { type: 'header', key: 'purpose', value: 'prefetch' },
      ],
    },

    {
      source:
        '/((?!api|_next/static|_next/image|favicon.ico|sitemap.xml|robots.txt).*)',
      has: [{ type: 'header', key: 'x-present' }],
      missing: [{ type: 'header', key: 'x-missing', value: 'prefetch' }],
    },
  ],
}
