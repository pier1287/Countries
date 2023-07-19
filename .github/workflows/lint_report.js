module.exports = async ({github, context, glob}) => {
    const xmlFiles = await glob('${github.workspace}/**/*.xml')

    await github.rest.issues.createComment({
        issue_number: context.issue.number,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: '⚠️ ${xmlFiles.length} xml filed found'
    })
}