# development 
FROM public.ecr.aws/docker/library/node:lts-alpine as development 
WORKDIR /app 
COPY package*.json ./
RUN npm install
CMD ["npm", "run", "serve"] 

# build 
FROM development as build 
COPY . . 
RUN npm run build
 
# production 
FROM public.ecr.aws/docker/library/nginx:alpine as production 
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]