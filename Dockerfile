# Adapted from
# https://github.com/vercel/next.js/blob/canary/examples/with-docker/Dockerfile

# Install dependencies only when needed
FROM node:20-alpine AS ndeps
# Check https://github.com/nodejs/docker-node/tree/b4117f9333da4138b03a546ec926ef50a31506c3#nodealpine to understand why libc6-compat might be needed.
RUN apk add --no-cache libc6-compat
WORKDIR /app

# Install dependencies based on the preferred package manager
COPY package.json pnpm-lock.yaml* ./
RUN corepack enable && pnpm i

FROM gradle:8.14.3-jdk11 AS kdeps
WORKDIR /app
COPY kotlin/*gradle* kotlin/kotlin-js-store ./kotlin/
WORKDIR /app/kotlin
RUN gradle clean assemble --no-daemon > /dev/null 2>&1 || true


# Rebuild the source code only when needed
FROM node:20-bullseye-slim AS builder
WORKDIR /app

# Install java11
RUN apt -q update && apt -y -q install --no-install-recommends openjdk-11-jdk-headless

COPY --from=ndeps /app/node_modules ./node_modules
COPY --from=kdeps /app/kotlin ./kotlin
COPY --from=kdeps /root/.gradle /root/.gradle
COPY . .

# Next.js collects completely anonymous telemetry data about general usage.
# Learn more here: https://nextjs.org/telemetry
# Uncomment the following line in case you want to disable telemetry during the build.
ENV NEXT_TELEMETRY_DISABLED 1

RUN corepack enable && pnpm run buildnd

# If using npm comment out above and use below instead
# RUN npm run build

# Production image, copy all the files and run next
FROM node:20-alpine AS runner
WORKDIR /app

ENV NODE_ENV production
# Uncomment the following line in case you want to disable telemetry during runtime.
ENV NEXT_TELEMETRY_DISABLED 1

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

# You only need to copy next.config.js if you are NOT using the default configuration
COPY --from=builder /app/next.config.js ./
COPY --from=builder /app/public ./public
COPY --from=builder /app/package.json ./package.json

# Automatically leverage output traces to reduce image size
# https://nextjs.org/docs/advanced-features/output-file-tracing
COPY --from=builder --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /app/.next/static ./.next/static

USER nextjs

EXPOSE 3000

ENV PORT 3000

CMD ["node", "server.js"]