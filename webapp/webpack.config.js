const path = require('path');

module.exports = (env, argv) => {
    const config = {
        entry: './src/main/js/app.tsx',
        devtool: 'sourcemaps',
        mode: 'development',
        output: {
            path: path.join(__dirname, 'src/main/resources'),
            filename: './static/built/bundle.js'
        },
        module: {
            rules: [
                {
                    test: path.join(__dirname, '.'),
                    exclude: /(node_modules)/,
                    use: [{
                        loader: 'babel-loader',
                        options: {
                            presets: ['@babel/preset-env', '@babel/preset-react']
                        }
                    }]
                },
                {
                    test: /\.css$/i,
                    use: ['style-loader', 'css-loader'],
                },
                {
                    test: /\.tsx?$/,
                    use: 'ts-loader',
                    exclude: /(node_modules)/
                },
            ]
        },
        "resolve": {
            extensions: ['.tsx', '.ts', '.js']
        }
    };

    if (argv.mode === 'production') {
        config.devtool = undefined;
        config.output.path = path.join(__dirname, 'build', 'resources', 'main')
    }

    return config;
};
