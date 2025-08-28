import tseslint from 'typescript-eslint';

export default tseslint.config(
  {
    ignores: ["node_modules", "android", "ios", "babel.config.js"],
  },
  {
    files: ['src/**/*.ts'],
    plugins: {
      '@typescript-eslint': tseslint.plugin,
    },
    languageOptions: {
      parser: tseslint.parser,
      parserOptions: {
        project: true,
      },
    },
    rules: {
      ...tseslint.configs.recommended.rules,
    },
  },
);
