# Pre Project Sizing Next.js Frontend

Build from cutting-edge-nextjs-app template

## Features

- Next.js 16 App Router
- `next-intl` locale routing (`en`, `vi`)
- Route groups for clear app boundaries
- Configurable landing-page render mode (Static / ISR / SSR)
- CSR dashboard with auth + example-entity CRUD
- Multi-adapter provider architecture selected by `DATA_PROVIDER` (`rest` | `firebase`)
- Sonner toasts (added 2026-04-06), themed with DaisyUI and i18n-aware API error mapping
- SEO baseline (`generateMetadata`, `sitemap.xml`, `robots.txt`, favicon/icons)
- Production SEO architecture and rollout guide in `docs/seo/architecture.md`
- TanStack Query + TanStack Form + Zod conventions for auth/example-entity flows
- Storybook coverage for shared and feature components

- Session route clears auth cookie for invalid/deactivated sessions.

## Getting Started

```bash
npm install
npm run dev
```

Open `http://localhost:3000`.

## Environment Variables

Copy `example.env` to `.env` and fill in values

`NEXT_PUBLIC_BASE_URL` is used for metadata, canonical URLs, sitemap, and robots.

## SEO setup

See `docs/seo/architecture.md` for canonical domain policy, public-only indexing scope, required metadata assets, and pre-launch validation checklist.
